package com.emotionalcart;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packagesOf = StockApplication.class)
public class PackageDependencyTests {

    private static final String APPLICATION = "..stock.application..";
    private static final String DOMAIN = "..stock.domain..";
    private static final String PRESENTATION = "..stock.presentation..";

    @ArchTest
    ArchRule presentationRule = ArchRuleDefinition.classes().that().resideInAPackage(PRESENTATION)
        .should().onlyBeAccessed().byClassesThat().resideInAnyPackage(PRESENTATION);

    @ArchTest
    ArchRule applicationRule = ArchRuleDefinition.classes().that().resideInAPackage(APPLICATION)
        .should().onlyBeAccessed().byClassesThat().resideInAnyPackage(APPLICATION, PRESENTATION);

    @ArchTest
    ArchRule domainRule = ArchRuleDefinition.classes().that().resideInAPackage(DOMAIN)
        .should().onlyBeAccessed().byClassesThat().resideInAnyPackage(APPLICATION, DOMAIN, PRESENTATION);

}
