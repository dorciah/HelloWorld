import { browser, by, element } from 'protractor';

export class AppPage {
  navigateTo() {
    return browser.get('/');
  }

  getH1Text() {
    return element(by.css('app-root h1')).getText();
  }

  getParaText() {
    return element(by.css('app-root p')).getText();
  }

  pauseBrowser(){
    browser.pause();
  }

}
