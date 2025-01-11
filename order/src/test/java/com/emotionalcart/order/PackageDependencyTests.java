package com.emotionalcart.order;

import com.emotionalcart.OrderApplication;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packagesOf = OrderApplication.class)
public class PackageDependencyTests {

    private static final String APPLICATION = "..order.application..";
    private static final String DOMAIN = "..order.domain..";
    private static final String INFRA = "..order.infra..";
    private static final String PRESENTATION = "..order.presentation..";

    @ArchTest
    ArchRule presentationRule = ArchRuleDefinition.classes().that().resideInAPackage(PRESENTATION)
        .should().onlyBeAccessed().byClassesThat().resideInAnyPackage(PRESENTATION);

    @ArchTest
    ArchRule applicationRule = ArchRuleDefinition.classes().that().resideInAPackage(APPLICATION)
        .should().onlyBeAccessed().byClassesThat().resideInAnyPackage(APPLICATION, PRESENTATION);

    @ArchTest
    ArchRule domainRule = ArchRuleDefinition.classes().that().resideInAPackage(DOMAIN)
        .should().onlyBeAccessed().byClassesThat().resideInAnyPackage(APPLICATION, DOMAIN, PRESENTATION);

    @ArchTest
    ArchRule infraRule = ArchRuleDefinition.classes().that().resideInAPackage(INFRA)
        .should().onlyBeAccessed().byClassesThat().resideInAnyPackage(APPLICATION, INFRA, PRESENTATION, DOMAIN);

}
