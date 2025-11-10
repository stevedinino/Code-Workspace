Feature: Launch Catalyst Legal Nurse website

  Scenario: Open homepage and verify title
    Given the user launches the Catalyst Legal Nurse website
    Then the page title should contain "Janet DiNino – Legal Nurse Consultant for Elder Care Litigation"
   
  Scenario: Verify links on the landing page
    Given the user launches the Catalyst Legal Nurse website
    Then the About link should navigate to the About page
    Then the Services link should navigate to the Services page
    And the Explore Services button should be visible
    Then the Testimonials link should navigate to the Testimonials page
    Then the Upload link should navigate to the Upload page
    Then the Contact link should navigate to the Contact page
    Then the Privacy link should navigate to the Privacy page
    Then the FAQ link should navigate to the FAQ page

