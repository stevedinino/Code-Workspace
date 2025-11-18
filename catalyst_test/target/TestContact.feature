Feature: Validate Catalyst Contact page using semantic keys

  Background:
    Given the user launches Catalyst web site
    And the XPath repository for "Contact" is loaded

  Scenario Outline: Interact with Contact page element and validate result
    When the user interacts with "<element_key>"
    Then the result of "<element_key>" should be validated

  Examples:
    | element_key               |
    | ContactHeader             |
    | IntroParagraph            |
    | EmailAddress              |
    | PhoneNumber               |
    | DownloadBusinessCardLink  |
    | BackToHomeLink            |
    | PrivacyLink               |
    | FAQLink                   |
    | FooterText                |