describe('Swagger Tests', () => {

  const host = Cypress.env('host') ? Cypress.env('host') : 'localhost';

  it('shows swagger', () => {
    cy.visit(`http://${host}:8000/api/yucca-api/swagger/views/swagger-ui/index.html`);
    cy.get('button').contains('Explore').click();
    cy.get('h2').contains('OpenAPI definition').should('not.be.null');
    cy.wait(1000);
    cy.get('div[class="servers"] > label > select > option').should('have.value', 'http://localhost:8000')
  });
})