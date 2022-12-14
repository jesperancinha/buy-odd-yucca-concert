describe('Kong Configuration', () => {
    const host = Cypress.env('host') ? Cypress.env('host') : 'localhost';

    it('Kong Metrics', () => {
        cy.request(`http://${host}:8001/metrics`).then(response =>{
            expect(response).not.to.be.null;
        });
    })
    it('Kong Plugins', () => {
        cy.request(`http://${host}:8001/plugins`).then(response =>{
            expect(response).not.to.be.null;
        })
    })
    it('Kong Services', () => {
        cy.request(`http://${host}:8001/services`).then(response =>{
            expect(response).not.to.be.null;
        });
    })
})