Feature: Validate Catalyst homepage using semantic keys

  Background:
    Given the user launches Catalyst web site
    And the XPath repository for "Index" is loaded

  Scenario Outline: Interact with homepage element
    When the user interacts with "<element_key>"
    Then the element "<element_key>" should be handled as expected

  Examples:
    | element_key                  |
    | HomeLogoLink                 |
    | AboutLink                    |
    | ServicesLink                 |
    | TestimonialsLink             |
    | UploadLink                   |
    | ContactLink                  |
    | PrivacyLink                  |
    | FAQLink                      |
    | ExploreServicesCTA           |
    | MainHeader                   |
    | SubtitleParagraph            |
    | WhyChooseHeader              |
    | StrategicServicesHeader      |
    | CredentialsList              |
    | ServicesList                 |
    | PrivacyPolicyLinkInList      |
    | SidebarImage                 |
    | FooterText                   |


    #| MenuToggleButton             |
    #| ConfirmationModal            |
    #| ModalHeader                  |
    #| ModalMessage                 |
    #| ModalConfirmButton           |
