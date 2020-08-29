"""
Given there is no order yet in the pool, the expected quantity is available 
in at least two manufacturers:
when create an order via API
Verify order is in DB
Verify order is generated on kafka orders topic
Verify the fulfillment plan is updated with the new order
"""

import requests, unittest, os

try:
    ORDER_MGR_URL = os.environ['ORDER_MGR_URL']
except KeyError:
    ORDER_MGR_URL = 'http://localhost:8080/orders'

class OrderHappyPath(unittest.TestCase):

    def test1_createOrder(self):
        print("Create a vaccine order to " + ORDER_MGR_URL)
        rep = requests.post(ORDER_MGR_URL)


if __name__ == '__main__':
    unittest.main()