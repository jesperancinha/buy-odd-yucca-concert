describe('Kong Configuration', () => {
    const host = Cypress.env('host') ? Cypress.env('host') : 'localhost';

    it('Kong Metrics', () => {
        cy.request(`http://${host}:8001/metrics`);
    })
    it('Kong Plugins', () => {
        cy.request(`http://${host}:8001/plugins`);
    })
    it('Kong Services', () => {
        cy.request(`http://${host}:8001/services`);
    })
})