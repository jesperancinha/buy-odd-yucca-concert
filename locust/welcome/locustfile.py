from locust import HttpUser, task


class BuyOddYuccaConcert(HttpUser):
    @task
    def yucca_ticket_welcome_message(self):
        self.client.get("http://localhost:8000/api/yucca-ticket")

    @task
    def yucca_parking_welcome_message(self):
        self.client.get("http://localhost:8000/api/yucca-parking")

    @task
    def yucca_catering_welcome_message(self):
        self.client.get("http://localhost:8000/api/yucca-catering")
