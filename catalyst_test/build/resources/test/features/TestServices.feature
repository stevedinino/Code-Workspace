Feature: Validate Services page elements using semantic keys

  @Skip
  Scenario Outline: Interact with Services page element and validate result
    Given the user launches Catalyst "Services" page
    And the XPath repository for "Services" is loaded
    When the user interacts with "<key>"
    Then the result of "<key>" should be validated

  Examples:
    | key                      |
    | HeaderLogoLink           |
    | PageTitleServices        |
    | SidebarHandshakeImage    |
    | FooterCopyright          |


  @Smoke
  Scenario Outline: Interact with Services page element and validate result
    Given the user launches Catalyst "Services" page
    And the XPath repository for "Services" is loaded
    When the user interacts with "<key>"
    Then the result of "<key>" should be validated

  Examples:
  | key                               |
  | HeaderLogoLink                    |
  | NavAboutLink                      |
  | NavServicesLink                   |
  | NavTestimonialsLink               |
  | NavUploadLink                     |
  | NavContactLink                    |
  | NavPrivacyLink                    |
  | NavFAQLink                        |
  | PageTitleServices                 |
  | PageSubtitle                      |
  | SectionMedicalRecordReview        |
  | SectionWoundCare                  |
  | SectionLongTermCare               |
  | SectionComplianceRegulatory       |
  | SectionExpertWitness              |
  | SidebarHandshakeImage             |
  | FooterCopyright                   |
  | MainContentArea                   |