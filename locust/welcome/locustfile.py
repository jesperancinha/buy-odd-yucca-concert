import requests
from locust import HttpUser, task


class BuyOddYuccaConcert(HttpUser):
    @task
    def yucca_ticket_welcome_message(self):
        r = requests.post('https://reqbin.com/echo/post/json', json={
            "Id": 78912,
            "Customer": "Jason Sweet",
            "Quantity": 1,
            "Price": 18.00
        })
        print(f"Status Code: {r.status_code}, Response: {r.json()}")
