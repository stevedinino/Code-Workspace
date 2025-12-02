Feature: Validate Contact page elements using semantic keys

  @Smoke
  Scenario Outline: Interact with Contact page element and validate result
    Given the user launches Catalyst "Contact" page
    And the XPath repository for "Contact" is loaded
    When the user interacts with "<key>"
    Then the result of "<key>" should be validated

  Examples:
    | key                    |
    | HeaderLogoLink         |
    | PageTitleContact       |
    | ContactPhoneNumber     |
    | FooterCopyright        |


  @Regression
  Scenario Outline: Interact with Contact page element and validate result
    Given the user launches Catalyst "Contact" page
    And the XPath repository for "Contact" is loaded
    When the user interacts with "<key>"
    Then the result of "<key>" should be validated

    Examples:
        | key                         |
        | HeaderLogoLink              |
        | MobileMenuToggle            |
        | NavAboutLink                |
        | NavServicesLink             |
        | NavTestimonialsLink         |
        | NavUploadLink               |
        | NavContactLink              |
        | NavPrivacyLink              |
        | NavFAQLink                  |
        | PageTitleContact            |
        | ContactEmailLink            |
        | ContactPhoneNumber          |
        | BusinessCardDownloadLink    |
        | FooterCopyright             |
        | MainContentArea             |
        | ContactInstructionsText     |