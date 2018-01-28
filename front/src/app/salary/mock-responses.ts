import {ClientResponse} from './clientResponse';

export const MOCK_RESPONSE_UK_BAD: any =
{
    "timestamp": 1517136855207,
    "status": 500,
    "error": "Internal Server Error",
    "exception": "java.lang.IllegalArgumentException",
    "message": "Daily salary is negative!",
    "path": "/api/exchange/request"
};

export const MOCK_RESPONSE_UK_GOOD: ClientResponse =
{
  "request": {
      "country": "UK",
      "amount": 420
  },
  "rate": 4.7957,
  "effectiveDate": "2018-01-26",
  "currency": "GBP",
  "results": [
      {
          "order": 0,
          "description": "gross",
          "amount": 9240,
          "diff": 0
      },
      {
          "order": 1,
          "description": "post-tax",
          "amount": 6930,
          "diff": 2310
      },
      {
          "order": 2,
          "description": "post-deduction",
          "amount": 6330,
          "diff": 600
      },
      {
          "order": 3,
          "description": "post-exchange",
          "amount": 30356.78,
          "diff": 0
      }
  ],
  "homeCurrency": "PLN"
};
