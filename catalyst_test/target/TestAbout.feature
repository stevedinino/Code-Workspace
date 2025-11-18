Feature: Validate Catalyst About page using semantic keys

  Background:
    Given the user launches Catalyst web site
    And the XPath repository for "About" is loaded

  Scenario Outline: Interact with About page element and validate result
    When the user interacts with "<element_key>"
    Then the result of "<element_key>" should be validated

  Examples:
    | element_key             |
    | AboutHeader             |
    | JanetIntroParagraph     |
    | CredentialsList         |
    | SCHouseCallsRole        |
    | ContactLink             |
    | PrivacyLink             |
    | FAQLink                 |
    | BackToHomeLink          |
    | FooterText              |