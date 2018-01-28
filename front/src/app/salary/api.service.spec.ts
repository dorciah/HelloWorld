import { TestBed, inject, async } from '@angular/core/testing';
import { HttpClientModule, HttpClient, HttpRequest, HttpParams } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ApiService } from './api.service';
import { environment } from '../../environments/environment';
import {ClientRequest} from './clientRequest';
import {ClientResponse} from './clientResponse';
import {MOCK_RESPONSE_UK_BAD, MOCK_RESPONSE_UK_GOOD} from './mock-responses';
import {MOCK_COUNTRIES} from './mock-countries';

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

  it(`ApiService should request a country list`,
    async(
      inject([ HttpTestingController,ApiService], (backend: HttpTestingController,api: ApiService) => {
        api.getAllCountries().subscribe((countries) => {
        expect(countries.length).toEqual(3);
        expect(countries[0].name).toEqual('UK');
        expect(countries[1].name).toEqual('Germany');
        expect(countries[2].name).toEqual('Poland');

      });

     backend.expectOne(API+'exchange/countries').flush(MOCK_COUNTRIES);

      })
    )
  );

   it(`ApiService should post a client request to the correct address with correct params/body`,
     async(
       inject([ HttpTestingController,ApiService], (backend: HttpTestingController,api: ApiService) => {
         const req: ClientRequest = new ClientRequest();
         req.amount = 420.00;
         req.country = "UK";

         api.sendRequest(req).subscribe((response: ClientResponse) => {
         });

         backend.expectOne((req: HttpRequest<any>) => {
           const body = new HttpParams({ fromString: req.body });
           const request: ClientRequest = req.body;
           return req.url === API+'exchange/request'
             && req.method === 'POST'
             && req.headers.get('Content-Type') === 'application/json'
             && request.amount === 420.00
             && request.country === 'UK'
             ;
         },'Post to request endpoint').flush(MOCK_RESPONSE_UK_GOOD);

          })
        )
      );

    it(`ApiService should post a valid client request and receive a valid response`,
      async(
        inject([ HttpTestingController,ApiService], (backend: HttpTestingController,api: ApiService) => {
          const req: ClientRequest = new ClientRequest();
          req.amount = 420.00;
          req.country = "UK";

          api.sendRequest(req).subscribe((response: ClientResponse) => {
            expect(response).toEqual(MOCK_RESPONSE_UK_GOOD);
          });

          backend.expectOne(API+'exchange/request').flush(MOCK_RESPONSE_UK_GOOD);

           })
         )
       );

       it(`ApiService should receive an error when posting a negative amount`,
         async(
           inject([ HttpTestingController,ApiService], (backend: HttpTestingController,api: ApiService) => {
             const req: ClientRequest = new ClientRequest();
             req.amount = -420.00;
             req.country = "UK";

             api.sendRequest(req).subscribe((response: ClientResponse) => {
               expect(response).toEqual(MOCK_RESPONSE_UK_BAD);
             });

             backend.expectOne(API+'exchange/request').flush(MOCK_RESPONSE_UK_BAD);

              })
            )
          );
});
