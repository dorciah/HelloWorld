import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient,HttpHeaders } from "@angular/common/http";
import { Observable} from 'rxjs/Observable';

import {Country} from './Country';
import {ClientResponse} from './clientResponse';

const API = environment.apiUrl;

@Injectable()
export class ApiService {

  constructor(private httpClient: HttpClient) { }

  private headers = new HttpHeaders().set('Content-Type', 'application/json');

  public getAllCountries():Observable<Country[]>{
    return this.httpClient.get<Country[]>(API+'exchange/countries',{headers:this.headers});
  }

  public sendRequest(request: any):Observable<ClientResponse>{
    return this.httpClient.post<ClientResponse>(API+'exchange/request',request,{headers:this.headers});
  }

}
