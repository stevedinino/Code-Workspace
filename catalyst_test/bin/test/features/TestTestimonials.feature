Feature: Validate Testimonials page elements using semantic keys

  @Smoke
  Scenario Outline: Interact with Testimonials page element and validate result
    Given the user launches Catalyst "Testimonials" page
    And the XPath repository for "Testimonials" is loaded
    When the user interacts with "<key>"
    Then the result of "<key>" should be validated

  Examples:
    | key                      |
    | HeaderLogoLink           |
    | PageTitleServices        |
    | SidebarHandshakeImage    |
    | FooterCopyright          |


  @Solo
  Scenario Outline: Interact with Testimonials page element and validate result
    Given the user launches Catalyst "Testimonials" page
    And the XPath repository for "Testimonials" is loaded
    When the user interacts with "<key>"
    Then the result of "<key>" should be validated

  Examples:
  | key                         |
  | HeaderLogoLink              |
  | NavAboutLink                |
  | NavServicesLink             |
  | NavTestimonialsLink         |
  | NavUploadLink               |
  | NavContactLink              |
  | NavPrivacyLink              |
  | NavFAQLink                  |
  | PageTitleTestimonials       |
  | TestimonialSection          |
  | Testimonial1Quote           |
  | Testimonial1Attribution     |
  | Testimonial2Quote           |
  | Testimonial2Attribution     |
  | Testimonial3Quote           |
  | Testimonial3Attribution     |
  | TestimonialAvatar1          |
  | TestimonialAvatar2          |
  | FooterCopyright             |
  | MainContentArea             |