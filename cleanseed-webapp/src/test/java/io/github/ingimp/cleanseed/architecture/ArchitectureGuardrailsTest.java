package io.github.ingimp.cleanseed.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "io.github.ingimp.cleanseed", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureGuardrailsTest {

    @ArchTest
    static final ArchRule application_must_be_framework_agnostic =
            noClasses().that().resideInAPackage("..application..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage(
                            "org.springframework..",
                            "jakarta.persistence..",
                            "..infrastructure..",
                            "..webapp.."
                    )
                    .as("Architecture Violation: Layer 'Application' must be framework-agnostic and must not depend on Spring/JPA/Infrastructure/Webapp.")
                    .because("The application layer contains pure use-case orchestration and must remain independent from frameworks and outer layers.");

    @ArchTest
    static final ArchRule webapp_controllers_must_not_be_transactional =
            noClasses().that().areAnnotatedWith(RestController.class)
                    .should().beAnnotatedWith(Transactional.class)
                    .as("Architecture Violation: Layer 'Webapp Controllers' must not declare @Transactional.")
                    .because("Transaction boundaries belong to dedicated facade/service classes in the outer layer, so controllers stay thin and focused on HTTP concerns.");

    @ArchTest
    static final ArchRule transactional_is_forbidden_outside_webapp =
            noClasses().that().resideOutsideOfPackage("..webapp..")
                    .should().beAnnotatedWith(Transactional.class)
                    .as("Architecture Violation: Only layer 'Webapp' may host @Transactional declarations.")
                    .because("Transaction demarcation is an outer-layer concern and must not leak into application/domain/infrastructure modules.");

    @ArchTest
    static final ArchRule transactional_methods_are_forbidden_outside_webapp =
            noClasses().that().resideOutsideOfPackage("..webapp..")
                    .should().haveMethodsAnnotatedWith(Transactional.class)
                    .as("Architecture Violation: Only layer 'Webapp' may host @Transactional methods.")
                    .because("Method-level transaction boundaries must remain in webapp facade/service entry points.");

    @ArchTest
    static final ArchRule transactional_webapp_classes_must_follow_facade_service_naming =
            classes().that().resideInAPackage("..webapp..")
                    .and().areAnnotatedWith(Transactional.class)
                    .should().haveSimpleNameMatching(".*(Facade|Service|ApplicationService)$")
                    .as("Architecture Violation: In layer 'Webapp', only Facade/Service classes may be class-level transactional.")
                    .because("Class-level @Transactional is allowed only on dedicated facade/service entry points.");

    @ArchTest
    static final ArchRule transactional_webapp_methods_must_follow_facade_service_naming =
            classes().that().resideInAPackage("..webapp..")
                    .and().haveMethodsAnnotatedWith(Transactional.class)
                    .should().haveSimpleNameMatching(".*(Facade|Service|ApplicationService)$")
                    .as("Architecture Violation: In layer 'Webapp', only Facade/Service classes may declare transactional methods.")
                    .because("Method-level @Transactional is allowed only on dedicated facade/service entry points.");
}
