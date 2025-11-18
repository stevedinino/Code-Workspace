Feature: Validate Catalyst FAQ page using semantic keys

  Background:
    Given the user launches Catalyst web site
    And the XPath repository for "FAQ" is loaded

  Scenario Outline: Interact with FAQ page element and validate result
    When the user interacts with "<element_key>"
    Then the result of "<element_key>" should be validated

  Examples:
    | element_key           |
    | FAQHeader             |
    | FAQQuestion1          |
    | FAQAnswer1            |
    | FAQQuestion2          |
    | FAQAnswer2            |
    | FAQQuestion3          |
    | FAQAnswer3            |
    | FAQQuestion4          |
    | FAQAnswer4            |
    | FAQQuestion5          |
    | FAQAnswer5            |
    | FAQQuestion6          |
    | FAQAnswer6            |
    | FAQQuestion7          |
    | FAQAnswer7            |
    | FAQQuestion8          |
    | FAQAnswer8            |
    | FAQQuestion9          |
    | FAQAnswer9            |
    | FAQQuestion10         |
    | FAQAnswer10           |
    | FAQQuestion11         |
    | FAQAnswer11           |
    | BackToHomeLink        |
    | ContactLink           |
    | PrivacyLink           |
    | FooterText            |