import { TestBed, inject, async } from '@angular/core/testing';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ApiService } from './api.service';
import { environment } from '../../environments/environment';
import {MOCK_MESSAGE} from './mock-messages';

const API = environment.apiUrl;

describe('ApiService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        HttpClientTestingModule
      ],providers: [
        ApiService
      ]
    });
  });

  afterEach(inject([HttpTestingController], (backend: HttpTestingController) => {
    backend.verify();
  }));

  it('should be created', inject([ApiService], (service: ApiService) => {
    expect(service).toBeTruthy();
  }));

  it(`ApiService should request a greeting`,
    async(
      inject([ HttpTestingController,ApiService], (backend: HttpTestingController,api: ApiService) => {
        api.getMessage(1).subscribe((next) => {
        expect(next.content).toEqual('dummy content');
      });

     backend.expectOne(API+'messages/1').flush(MOCK_MESSAGE);

      })
    )
  );

});
