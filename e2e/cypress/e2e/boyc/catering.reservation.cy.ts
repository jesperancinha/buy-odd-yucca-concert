describe('Catering welcom message', () => {

    const host = Cypress.env('host') ? Cypress.env('host') : 'localhost';

    it('passes', () => {
        cy.request(`http://${host}:8000/api/yucca-catering`, {
            headers: {
                "content-type": "text/html"
            }
        }).then(response => {
            expect(response.body.second).to.be.eq("Welcome to the Catering service");
        })
    })
})