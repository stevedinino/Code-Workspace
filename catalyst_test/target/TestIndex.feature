Feature: Validate homepage elements using semantic keys

  Scenario Outline: Interact with homepage element and validate result
    Given the user launches Catalyst web site
    And the XPath repository for "Index" is loaded
    When the user interacts with "<key>"
    Then the result of "<key>" should be validated

    Examples:
      | key                      |
      | HomeLogoLink            |
      | ExploreServicesCTA      |
      | MainHeadline            |
      | IntroParagraph          |
      | AboutLink               |
      | TestimonialsLink        |
      | FAQLink                 |
      | ContactLink             |
      | FooterAboutLink         |
      | FooterTestimonialsLink  |
      | FooterFAQLink           |
      | FooterContactLink       |
      | HeaderSubtext           |
      | ServicesPreviewHeader   |
      | ServicesPreviewText     |
      | WhyChooseHeader         |
      | WhyChooseText           |
      | TestimonialPreviewHeader|
      | TestimonialPreviewText  |
      | FAQPreviewHeader        |
      | FAQPreviewText          |
      | ContactPreviewHeader    |
      | ContactPreviewText      |
      | PrivacyPolicyLinkInList |
      | SidebarImage            |
      | FooterText              |
      | LegalDisclaimer         |
      | CopyrightNotice         |
      | ScrollToTopButton       |
      | StickyNavOnScroll       |
      | HeroBackgroundImage     |