Feature: Validate Index page elements using semantic keys

  @Smoke
  Scenario Outline: Interact with Index page element and validate result
    Given the user launches Catalyst "index" page
    And the XPath repository for "Index" is loaded
    When the user interacts with "<key>"
    Then the result of "<key>" should be validated

  Examples:
    | key                          |
    | title                        |
    | header                       |
    | main_nav                     |
    | footer                       |


  @Critical
  Scenario Outline: Interact with Index page element and validate result
    Given the user launches Catalyst "index" page
    And the XPath repository for "Index" is loaded
    When the user interacts with "<key>"
    Then the result of "<key>" should be validated

  Examples:
    | key                          |
    | title                        |
    | link_favicon                 |
    | header                       |
    | logo_image_visible           |
    | main_nav                     |
    | main_heading                 |
    | h2_why_attorneys             |
    | section_strategic_services   |
    | h2_strategic_services        |
    | cta_explore_services         |
    | sidebar_image_visible        |
    | footer                       |


  @Regression
  Scenario Outline: Interact with Index page element and validate result
    Given the user launches Catalyst "index" page
    And the XPath repository for "Index" is loaded
    When the user interacts with "<key>"
    Then the result of "<key>" should be validated

  Examples:
    | key                          |
    | title                        |
    | link_favicon                 |
    | script_schema_json           |
    | header                       |
    | logo_container               |
    | logo_link                    |
    | logo_image                   |
    | logo_image_visible           |
    | nav_wrapper                  |
    | main_nav                     |
    | nav_links_list               |
    | nav_about_li                 |
    | nav_about_link               |
    | nav_services_li              |
    | nav_services_link            |
    | nav_testimonials_li          |
    | nav_testimonials_link        |
    | nav_upload_li                |
    | nav_upload_link              |
    | nav_contact_li               |
    | nav_contact_link             |
    | nav_privacy_li               |
    | nav_privacy_link             |
    | nav_faq_li                   |
    | nav_faq_link                 |
    | page_layout                  |
    | main_content                 |
    | main_heading                 |
    | main_subtitle                |
    | section_why_attorneys        |
    | h2_why_attorneys             |
    | list_why_attorneys           |
    | why_item_board_certified     |
    | why_item_legal_nurse         |
    | why_item_nursing_admin       |
    | why_item_wound_care          |
    | why_item_leadership          |
    | why_item_privacy_policy      |
    | link_privacy_policy_why      |
    | section_strategic_services   |
    | h2_strategic_services        |
    | list_strategic_services      |
    | service_medical_review       |
    | service_wound_care           |
    | service_long_term_care       |
    | service_compliance           |
    | service_expert_witness       |
    | cta_explore_services_wrapper |
    | cta_explore_services         |
    | sidebar_image_container      |
    | sidebar_image                |
    | sidebar_image_visible        |
    | footer                       |
    | footer_copyright             |
    | copyright_text               |