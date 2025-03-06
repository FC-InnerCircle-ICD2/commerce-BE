package com.emotionalcart.application;

import com.emotionalcart.domain.entity.*;
import com.emotionalcart.infra.StockProvider;
import com.emotionalcart.infra.properties.CoupangCrawlerProperties;
import com.emotionalcart.infra.repository.CategoryRepository;
import com.emotionalcart.infra.repository.ProductRepository;
import com.emotionalcart.infra.repository.ProviderRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.emotionalcart.domain.entity.OptionType.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoupangCrawler {

    private final CoupangCrawlerProperties coupangCrawlerProperties;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProviderRepository providerRepository;
    private final RedisProductService redisProductService;
    private final StockProvider stockProvider;
    private final RestTemplate restTemplate;
    private WebDriver driver;

    public void initDriver() {
        // ChromeDriver 자동 다운로드 및 실행
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless"); // 브라우저 창 없이 실행 (필요하면 제거)
        options.addArguments("--disable-blink-features=AutomationControlled"); // 자동화 탐지 방지
        options.addArguments("start-maximized"); // 창 최대화
        options.addArguments("disable-infobars"); // 정보 표시줄 제거
        options.addArguments("--disable-extensions"); // 확장 프로그램 비활성화
        options.addArguments("--incognito"); // 시크릿 모드
        options.addArguments("--disable-gpu"); // GPU 가속 비활성화
        options.addArguments("--no-sandbox"); // 샌드박스 모드 비활성화
        options.addArguments("--disable-dev-shm-usage"); // 공유 메모리 사용 안 함
        options.addArguments(
            "user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.of(10, TimeUnit.SECONDS.toChronoUnit()));
        // WebDriver 실행 후, 아래 코드 추가
        ((JavascriptExecutor)driver).executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
    }

    @Transactional
    public void crawl() {
        initDriver();
        String url = coupangCrawlerProperties.getDefaultCategoryUrl();
        log.info("Crawling: {}", url);
        Optional<Category> byId = categoryRepository.findById(Long.parseLong(coupangCrawlerProperties.getDefaultCategory()));
        Category category;
        if (byId.isPresent()) {
            category = byId.get();
        } else {
            category = Category.of(coupangCrawlerProperties.getDefaultCategoryName());
            categoryRepository.saveAndFlush(category);
        }
        try {
            driver.get(url);

            // 특정 XPath를 사용하여 요소 선택
            List<WebElement> elements =
                driver.findElements(By.xpath("//*[@id=\"searchOptionForm\"]/div/div/div[1]/div[1]/div[1]/map/area"));
            List<String> categories = new ArrayList<>();
            for (WebElement element : elements) {
                String href = element.getAttribute("href");
                categories.add(href);
            }
            /*for (int i = 1; i < 2; i++) {
                getProductByHref(categories.get(i), category);
            }*/
            for (String href : categories) {
                getProductByHref(href, category);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error while crawling: {}", e.getMessage());
        } finally {
            driver.quit(); // 브라우저 종료
        }
    }

    private void getProductByHref(String href, Category parentCategory) {
        initDriver();
        try {
            driver.get(href);
            Optional<String> optionalCategoryId = getCategoryIdByHref(href);
            String text = driver.findElement(By.cssSelector(".newcx-product-list-title")).getText();
            if (optionalCategoryId.isEmpty()) {
                return;
            }
            Category category = Category.of(text, parentCategory, parentCategory.getDepth() + 1);
            categoryRepository.saveAndFlush(category);
            List<WebElement> products = driver.findElements(By.cssSelector("li.baby-product"));
            for (WebElement productElement : products) {
                WebElement linkElement = productElement.findElement(By.cssSelector(".baby-product-link"));
                String detailLink = linkElement.getAttribute("href");
                log.info("detailLink: {}", detailLink);
                Optional<String> optionalProductId = getProductIdByHref(detailLink);
                if (optionalProductId.isPresent()) {
                    // 상품명
                    String productName = productElement.findElement(By.cssSelector(".name")).getText();

                    // 이미지 URL
                    String imageUrl = productElement.findElement(By.cssSelector(".image img")).getAttribute("src");

                    // 가격
                    String price = productElement.findElement(By.cssSelector(".price-value")).getText();
                    if (price.contains(",")) {
                        price = price.replace(",", "");
                    }
                    // 결과 출력
                    Product product = Product.of(category, imageUrl, productName, Integer.parseInt(price));
                    ProductImage productImage = ProductImage.of(product, imageUrl, ImageType.MAIN);
                    product.addImages(productImage);
                    getDetailOptions(detailLink, product);
                    log.info("상품명: {}", productName);
                    log.info("이미지 URL: {} ", imageUrl);
                    log.info("가격: {}", price);
                    log.info("--------------------------------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to connect: {}", href);
        } finally {
            driver.quit(); // 브라우저 종료
        }
    }

    private void getDetailOptions(String detailLink, Product product) {
        String nowTab = driver.getWindowHandle();
        driver.switchTo().newWindow(WindowType.TAB);// 새 탭으로 이동
        try {
            driver.get(detailLink);
            setProviderAndSaveProduct(product);
            WebElement element = driver.findElement(By.xpath("//*[@id=\"productDetail\"]"));
            List<WebElement> detailImageElements = element.findElements(By.xpath(".//img"));
            for (WebElement detailImageElement : detailImageElements) {
                String imageUrl = detailImageElement.getAttribute("src");
                ProductImage productImage = ProductImage.of(product, imageUrl, ImageType.DETAIL);
                product.addImages(productImage);
            }
            //*[@id="productDetail"]/div[1]/div/div/div/div/img[1]
            WebElement descriptionElement = driver.findElement(By.cssSelector(".prod-description-attribute"));
            List<WebElement> descriptionElementElements = descriptionElement.findElements(By.cssSelector(".prod-attr-item"));
            StringBuilder description = new StringBuilder();
            for (WebElement descriptionElementElement : descriptionElementElements) {
                description.append(descriptionElementElement.getText()).append("\n");
            }
            log.error("상품 설명: {}", description);
            product.setDescription(description.toString());
            List<WebElement> optionContainers = driver.findElements(By.cssSelector("#optionWrapper > div"));
            for (WebElement optionContainer : optionContainers) {

                String optionTypeId = optionContainer.getAttribute("data-attribute-type-id");
                log.info("옵션 ID: {}", optionTypeId);
                ProductOption productOption = ProductOption.of(product);
                List<ProductOptionDetail> productOptionDetails = new ArrayList<>();
                // Dropdown-Select 타입 옵션
                if (Objects.requireNonNull(optionContainer.getAttribute("class")).contains("Dropdown-Select")) {
                    setDropDown(optionContainer, productOption, productOptionDetails);
                }
                // Text-Select 타입 옵션
                else if (Objects.requireNonNull(optionContainer.getAttribute("class")).contains("Text-Select--Container")) {
                    setTextSelect(optionContainer, productOption, productOptionDetails);
                }
                // Image-Select 타입 옵션
                else if (Objects.requireNonNull(optionContainer.getAttribute("class")).contains("Image-Select__Container")) {
                    setImageSelect(optionContainer, productOption, productOptionDetails);
                } else if (Objects.requireNonNull(optionContainer.getAttribute("class")).contains("single-attribute__textLabel")) {
                    setSingleText(optionContainer, productOption, productOptionDetails);
                }
            }
            product.addReviewStatistics(ReviewStatistic.of(product));
            productRepository.saveAndFlush(product);
            stockProvider.generateOptionCombinations(product.getId());
            /*RedisProduct redisProduct = RedisProduct.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId()).build();
            VectorProduct vectorProduct =
                VectorProduct.builder().name(redisProduct.getName()).description(redisProduct.getDescription()).build();
            VectorEntity entity =
                restTemplate.postForObject("http://127.0.0.1:8000/generate_vector", vectorProduct, VectorEntity.class);
            assert entity != null;
            log.error("entity: {}", entity.getVector());
            redisProduct.setVector(entity.getVector());
            redisProductService.saveProduct(redisProduct);*/
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to connect: {}", detailLink);
        }
        // 새 탭 닫기
        driver.close();

        // 원래 탭으로 돌아오기
        driver.switchTo().window(nowTab);
    }

    private static void setSingleText(WebElement optionContainer,
                                      ProductOption productOption,
                                      List<ProductOptionDetail> productOptionDetails) {
        WebElement iElement = optionContainer.findElement(By.cssSelector("i"));
        String optionName =
            optionContainer.getText().replace(iElement.getText(), "").strip().replace(":", "").strip();
        productOption.defineName(optionName);
        productOption.defineType(SINGLE_TEXT);
        productOptionDetails.add(ProductOptionDetail.of(iElement.getText(), productOption));
    }

    private static void setImageSelect(WebElement optionContainer,
                                       ProductOption productOption,
                                       List<ProductOptionDetail> productOptionDetails) {
        List<WebElement> items = optionContainer.findElements(By.cssSelector(".Image-Select__Item"));
        WebElement optionNameElement = optionContainer.findElement(By.cssSelector(".imageLabel"));
        WebElement iElement = optionNameElement.findElement(By.cssSelector("i"));
        String optionName =
            optionNameElement.getText().replace(iElement.getText(), "").strip().replace(":", "").strip();
        productOption.defineName(optionName);
        productOption.defineType(IMAGE_SELECT);
        for (WebElement item : items) {
            item.click();
            String attributeId = item.getAttribute("data-attribute-id");
            String name = optionNameElement.findElement(By.cssSelector("i")).getText();
            String imageUrl = item.getAttribute("data-origin-image-url");
            productOptionDetails.add(ProductOptionDetail.of(name, imageUrl, productOption));
            System.out.println(" - [Image] 옵션: ID=" + attributeId + ", 이미지 URL=" + imageUrl);
        }
    }

    private static void setTextSelect(WebElement optionContainer,
                                      ProductOption productOption,
                                      List<ProductOptionDetail> productOptionDetails) {
        WebElement optionNameElement = optionContainer.findElement(By.cssSelector(".textLabel"));
        WebElement iElement = optionNameElement.findElement(By.cssSelector("i"));
        String optionName = optionNameElement.getText().replace(iElement.getText(), "").strip().replace(":", "").strip();

        productOption.defineName(optionName);
        productOption.defineType(TEXT_SELECT);
        List<WebElement> items = optionContainer.findElements(By.cssSelector(".Text-Select__Item"));
        for (WebElement item : items) {
            String attributeId = item.getAttribute("data-attribute-id");
            String name = item.findElement(By.cssSelector(".Text-Select__Item__Text")).getText().trim();
            productOptionDetails.add(ProductOptionDetail.of(name, productOption));
            System.out.println(" - [Text] 옵션: ID=" + attributeId + ", 텍스트=" + name);
        }
    }

    private static void setDropDown(WebElement optionContainer,
                                    ProductOption productOption,
                                    List<ProductOptionDetail> productOptionDetails) {
        List<WebElement> items = optionContainer.findElements(By.cssSelector(".Dropdown-Select__Dropdown__Item"));
        String optionName = optionContainer.findElement(By.id("Dropdown-Select__Attr-id")).getText();
        productOption.defineName(optionName);
        productOption.defineType(DROPDOWN);
        optionContainer.findElement(By.cssSelector(".Dropdown-Select__Label__Container")).click();
        for (WebElement item : items) {
            String attributeId = item.getAttribute("data-attribute-id");
            String name = item.getText().trim();
            productOptionDetails.add(ProductOptionDetail.of(name, productOption));
            System.out.println(" - [Dropdown] 옵션: ID=" + attributeId + ", 텍스트=" + name);
        }
        optionContainer.findElement(By.cssSelector(".Dropdown-Select__Label__Container")).click();
    }

    private void setProviderAndSaveProduct(Product product) {
        String providerName;
        WebElement vendorElement = driver.findElement(By.cssSelector(".prod-vendor-container"));
        if (Objects.equals(vendorElement.getAttribute("style"), "display: none;")) {
            providerName = driver.findElement(By.xpath("//*[@id=\"itemBrief\"]/div/table/tbody/tr[2]/td[2]")).getText();
        } else {
            WebElement vendorInfoElement = vendorElement.findElement(By.cssSelector(".prod-vendor > .prod-sale-vendor"));
            try {
                WebElement aElement = vendorInfoElement.findElement(By.tagName("a"));
                providerName = aElement.getText();
            } catch (Exception e) {
                providerName = vendorInfoElement.getText().replace("판매자: ", "").trim();
            }
        }
        Optional<Provider> optionalProvider = providerRepository.findByName(providerName);
        if (optionalProvider.isPresent()) {
            product.setProvider(optionalProvider.get());
        } else {
            Provider provider = Provider.of(providerName);
            providerRepository.save(provider);
            product.setProvider(provider);
        }
    }

    private Optional<String> getCategoryIdByHref(String url) {
        Pattern pattern = Pattern.compile("/np/categories/(\\d+)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
    }

    private Optional<String> getProductIdByHref(String url) {
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        Pattern pattern = Pattern.compile("/vp/products/(\\d+)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
    }

}
