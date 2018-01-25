import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient,HttpHeaders } from "@angular/common/http";
import { Observable} from 'rxjs/Observable';

import {Message} from './Message';

const API = environment.apiUrl;

@Injectable()
export class ApiService {

  constructor(private httpClient: HttpClient) { }

  private headers = new HttpHeaders().set('Content-Type', 'application/json');

  public getAllMessages():Observable<Message[]>{
    return this.httpClient.get<Message[]>(API+'messages',{headers:this.headers});
  }

  public getMessage(id: number):Observable<Message>{
    return this.httpClient.get<Message>(API+'messages/'+id,{headers:this.headers});
  }

}
