import { AppPage } from './app.po';

describe('front App', () => {
  let page: AppPage;

  beforeEach(() => {
    page = new AppPage();
  });

  it('should display welcome h1', () => {
    page.navigateTo();
    expect(page.getH1Text()).toEqual('Welcome to The Base Application (TBA)!');
  });

  it('should display dummy paragraph text', () => {
    page.navigateTo();
    expect(page.getParaText()).toEqual('More text here');
  });

});
