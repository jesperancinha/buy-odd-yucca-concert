import requests
from locust import HttpUser, task


class BuyOddYuccaConcert(HttpUser):
    @task
    def yucca_ticket_welcome_message(self):
        r = requests.post('http://localhost:8000/api/yucca-api/api', json=
        {
            "name": "Budinok",
            "address": "Aдреса Matin",
            "birthDate": "1979-07-17",
            "concertDays": [
                {
                    "concertId": "5359A368-CA49-4027-BC25-F375E3EA2463"
                }
            ],
            "meals": [
                {
                    "mealId": "59B97053-37CF-4FAF-AB50-E77CEF8E8CC8"
                }
            ],
            "drinks": [
                {
                    "drinkId": "B2A5E349-76E7-4CD6-8105-308D1BC94953"
                }
            ],
            "parkingReservation": [
                {
                    "carParkingId": 1
                }
            ]
        })
        print(f"Status Code: {r.status_code}, Response: {r.json()}")
