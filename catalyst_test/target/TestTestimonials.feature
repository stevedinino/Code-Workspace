Feature: Validate Catalyst Testimonials page using semantic keys

  Background:
    Given the user launches Catalyst web site
    And the XPath repository for "Testimonials" is loaded

  Scenario Outline: Interact with Testimonials page element and validate result
    When the user interacts with "<element_key>"
    Then the result of "<element_key>" should be validated

  Examples:
    | element_key             |
    | TestimonialsHeader      |
    | TestimonialOneQuote     |
    | TestimonialTwoQuote     |
    | TestimonialThreeQuote   |
    | BackToHomeLink          |
    | ContactLink             |
    | PrivacyLink             |
    | FAQLink                 |
    | FooterText              |