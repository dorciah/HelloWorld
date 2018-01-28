
import {Country} from './country';

export const MOCK_COUNTRY: Country =
  {id: 1,name: 'UK', currency: 'GBP',tax: 0.25, deductions: 1000.0};

export const MOCK_COUNTRIES: Country[] =
  [ {id: 1,name: 'UK', currency: 'GBP',tax: 0.25, deductions: 600.0},
    {id: 2,name: 'Germany', currency: 'EUR',tax: 0.20, deductions: 800.0},
    {id: 3,name: 'Poland', currency: 'PLN',tax: 0.19, deductions: 1200.0}
  ];
