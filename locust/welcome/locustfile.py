import requests
from locust import HttpUser, task


def send_payload(name, address, birth_date, concert_id, drink_id, meal_id):
    payload = {
        "name": name,
        "address": address,
        "birthDate": birth_date,
        "concertDays": [
            {
                "concertId": concert_id
            }
        ],
        "meals": [
            {
                "mealId": meal_id
            }
        ],
        "drinks": [
            {
                "drinkId": drink_id
            }
        ],
        "parkingReservation": [
            {
                "carParkingId": 1
            }
        ]
    }
    r = requests.post('http://localhost:8000/api/yucca-api/api', json=payload)
    print(
        f"Person: {name} living in {address} born on {birth_date}, just reserved concert {concert_id} with drink {drink_id} and meal {meal_id}, Status Code: {r.status_code}, Response: {r.json()}")


class BuyOddYuccaConcert(HttpUser):
    @task
    def yucca_ticket_welcome_message(self):
        send_payload(
            name="будинок",
            address="будинок Адреса",
            birth_date="1991-08-24",
            concert_id="5359A368-CA49-4027-BC25-F375E3EA2463",
            drink_id="B2A5E349-76E7-4CD6-8105-308D1BC94953",
            meal_id="59B97053-37CF-4FAF-AB50-E77CEF8E8CC8")
        send_payload(
            name="Home",
            address="Home Address",
            birth_date="1979-01-01",
            concert_id="2E4522B1-D9FF-4B2B-9FFA-052CBAD9D5F2",
            drink_id="B2A5E349-76E7-4CD6-8105-308D1BC94953",
            meal_id="59B97053-37CF-4FAF-AB50-E77CEF8E8CC8")
