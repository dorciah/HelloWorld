import { AppPage } from './app.po';
import { browser, by, element } from 'protractor';

describe('front App', () => {
  let page: AppPage;

  beforeEach(() => {
    page = new AppPage();
  });

  it('should display welcome h1', () => {
    page.navigateTo();
    expect(page.getH1Text()).toEqual('Welcome to The Net Salary App!');
  });

  it('should display dummy paragraph text', () => {
    page.navigateTo();
    expect(page.getParaText()).toEqual('Choose a country from the dropdown and your day rate');
  });

  it('should have standard 3 countries in dropdown',() => {
      page.navigateTo();
      element(by.id('country')).getText().then(function(text) {
        console.log(text.trim());
        expect(text.trim()).toEqual("UK\nGermany\nPoland");
      });
  });

  it('Form submitted with blank country should give country warning',() => {
      page.navigateTo();
      element(by.id('submit')).click().then(function() {
        element(by.id('warningText')).getText().then(function(text) {
          expect(text.trim()).toEqual("Invalid Country name in request");
        });
      });
  });

  it('Form submitted with negative amount should give warning',() => {
      page.navigateTo();
      element(by.id('country')).sendKeys("UK");
      element(by.id('amount')).clear()
      element(by.id('amount')).sendKeys("-400.00");
      element(by.id('submit')).click().then(function() {
        element(by.id('warningText')).getText().then(function(text) {
          expect(text.trim()).toEqual("Daily salary is negative!");
        });
      });
  });

  it('Form submitted with UK should show results with exchange',() => {
      page.navigateTo();
      element(by.id('country')).sendKeys("UK");
      element(by.id('amount')).sendKeys("400.00");
      element(by.id('submit')).click().then(function() {

        element(by.id('result-request')).getText().then(function(text) {
          expect(text.trim()).toEqual("Request Details: 400 GBP per day in UK");
        });
        element(by.id('result-rate')).getText().then(function(text) {
          expect(text.trim().startsWith("Exchange Details: Using rate")).toBeTruthy();
        });
        element(by.id('row-0')).getText().then(function(text) {
          expect(text.trim()).toEqual("gross");
        });
        element(by.id('row-1')).getText().then(function(text) {
          expect(text.trim()).toEqual("post-tax");
        });
        element(by.id('row-2')).getText().then(function(text) {
          expect(text.trim()).toEqual("post-deduction");
        });
        element(by.id('row-3')).getText().then(function(text) {
          expect(text.trim()).toEqual("post-exchange");
        });
        element(by.id('amount-0')).getText().then(function(text) {
          expect(text.trim()).toEqual("8800 GBP");
        });
        element(by.id('amount-1')).getText().then(function(text) {
          expect(text.trim()).toEqual("6600 GBP");
        });
        element(by.id('amount-2')).getText().then(function(text) {
          expect(text.trim()).toEqual("6000 GBP");
        });
        element(by.id('amount-3')).getText().then(function(text) {
          expect(text.trim()).toEqual("28774.2 PLN");
        });
        element(by.id('diff-0')).getText().then(function(text) {
          expect(text.trim()).toEqual("0 GBP");
        });
        element(by.id('diff-1')).getText().then(function(text) {
          expect(text.trim()).toEqual("2200 GBP");
        });
        element(by.id('diff-2')).getText().then(function(text) {
          expect(text.trim()).toEqual("600 GBP");
        });
      });

  });

  it('Form submitted with Poland should show limited results without exchange',() => {
      page.navigateTo();
      element(by.id('country')).sendKeys("Poland");
      element(by.id('amount')).sendKeys("400.00");
      element(by.id('submit')).click().then(function() {

        element(by.id('result-request')).getText().then(function(text) {
          expect(text.trim()).toEqual("Request Details: 400 PLN per day in Poland");
        });

        // check that rate is missing for Polish request
        expect(element(by.id('result-rate')).isPresent()).toBeFalsy();

        element(by.id('row-0')).getText().then(function(text) {
          expect(text.trim()).toEqual("gross");
        });
        element(by.id('row-1')).getText().then(function(text) {
          expect(text.trim()).toEqual("post-tax");
        });
        element(by.id('row-2')).getText().then(function(text) {
          expect(text.trim()).toEqual("post-deduction");
        });

        // check that rate is missing for Polish request
        expect(element(by.id('row-3')).isPresent()).toBeFalsy();

        element(by.id('amount-0')).getText().then(function(text) {
          expect(text.trim()).toEqual("8800 PLN");
        });
        element(by.id('amount-1')).getText().then(function(text) {
          expect(text.trim()).toEqual("7128 PLN");
        });
        element(by.id('amount-2')).getText().then(function(text) {
          expect(text.trim()).toEqual("5928 PLN");
        });

        // check that rate is missing for Polish request
        expect(element(by.id('amount-3')).isPresent()).toBeFalsy();

        element(by.id('diff-0')).getText().then(function(text) {
          expect(text.trim()).toEqual("0 PLN");
        });
        element(by.id('diff-1')).getText().then(function(text) {
          expect(text.trim()).toEqual("1672 PLN");
        });
        element(by.id('diff-2')).getText().then(function(text) {
          expect(text.trim()).toEqual("1200 PLN");
        });

        // check that rate is missing for Polish request
        expect(element(by.id('diff-3')).isPresent()).toBeFalsy();
      });

  });

  it('Clear button clears all results',() => {
      page.navigateTo();
      element(by.id('country')).sendKeys("UK");
      element(by.id('amount')).sendKeys("400.00");
      element(by.id('submit')).click().then(function() {

        // check results are visible
        expect(element(by.id('results')).isPresent()).toBeTruthy();

        element(by.id('clear')).click().then(function() {
          expect(element(by.id('results')).isPresent()).toBeFalsy();
        });

      });
    });
});
