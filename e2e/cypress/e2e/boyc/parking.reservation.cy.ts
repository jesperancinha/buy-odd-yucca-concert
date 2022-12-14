describe('Parking Welcome Message', () => {
  const host = Cypress.env('host') ? Cypress.env('host') : 'localhost';

  it('passes', () => {
    cy.request(`http://${host}:8000/api/yucca-parking`, {
      headers: {
        "content-type": "text/html"
      }
    }).then(response => {
      expect(response.body.second).to.be.eq("Welcome to the Parking service");
    })
  })
})