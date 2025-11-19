Feature: Validate About page elements using semantic keys

  @Smoke
  Scenario Outline: Interact with About page element and validate result
    Given the user launches Catalyst "About" page
    And the XPath repository for "About" is loaded
    When the user interacts with "<key>"
    Then the result of "<key>" should be validated

  Examples:
    | key                    |
    | HomeLogoLink           |
    | PageHeading            |
    | LogoImageVisible       |
    | FooterCopyright        |


  @Regression
  Scenario Outline: Interact with About page element and validate result
    Given the user launches Catalyst "About" page
    And the XPath repository for "About" is loaded
    When the user interacts with "<key>"
    Then the result of "<key>" should be validated

  Examples:
    | key                    |
    | HomeLogoLink           |
    | NavAboutLink           |
    | NavServicesLink        |
    | NavTestimonialsLink    |
    | NavUploadLink          |
    | NavContactLink         |
    | NavPrivacyLink         |
    | NavFaqLink             |
    | MobileMenuToggle       |
    | LogoImageVisible       |
    | LogoImageAltText       |
    | JanetHeroImageVisible  |
    | JanetHeroImageAltText  |
    | PageHeading            |
    | BioParagraph01         |
    | BioParagraph02         |
    | BioParagraph03         |
    | BioParagraph04         |
    | BioParagraph05         |
    | BioParagraph06         |
    | BioParagraph07         |
    | LicensesHeading        |
    | LicensesList           |
    | FooterCopyright        |