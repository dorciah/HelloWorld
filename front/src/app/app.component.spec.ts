import { TestBed, async } from '@angular/core/testing';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import {By} from '@angular/platform-browser';

import { AppComponent } from './app.component';
import {ApiService} from './salary/api.service';

describe('AppComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule,
        HttpClientTestingModule,
        FormsModule],
      declarations: [
        AppComponent
      ],providers:[ApiService]
    }).compileComponents();
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

  it('should render title in a h1 tag', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('h1').textContent).toContain('Welcome to The Net Salary App!');
  }));

  it('Form should be visible', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const form = fixture.debugElement.query(By.css('#form'));
    expect(form.nativeElement.hasAttribute('hidden')).toEqual(false);
  }));

  it('warning should be null', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const warningDiv = fixture.debugElement.query(By.css('#warning'));
    expect(warningDiv).toBeNull();
  }));

  it('initial results should be null', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const results = fixture.debugElement.query(By.css('#results'));
    expect(results).toBeNull();
  }));

});
