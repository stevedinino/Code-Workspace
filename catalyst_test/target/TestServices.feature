Feature: Validate Catalyst Services page using semantic keys

  Background:
    Given the user launches Catalyst web site
    And the XPath repository for "Services" is loaded

  Scenario Outline: Interact with services page element and validate result
    When the user interacts with "<element_key>"
    Then the result of "<element_key>" should be validated

  Examples:
    | element_key                |
    | StrategicServicesHeader    |
    | MedicalRecordReviewHeader  |
    | WoundCareSupportHeader     |
    | LongTermCareHeader         |
    | ComplianceReviewHeader     |
    | ExpertWitnessPrepHeader    |
    | CaseReviewImage            |
    | ContactCTAButton           |
    | BackToHomeLink             |
    | FAQLink                    |
    | PrivacyLink                |
    | FooterText                 |