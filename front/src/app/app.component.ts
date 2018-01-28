import { Component, OnInit } from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';

import {ApiService} from './salary/api.service';
import {Country} from './salary/country';
import {ClientRequest} from './salary/clientRequest';
import {ClientResponse} from './salary/clientResponse';
import {Result} from './salary/result';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  public title: String = 'The Net Salary App';
  public tagLine: String = 'Choose a country from the dropdown and your day rate';

  public countries:Country[];
  public model: ClientRequest;
  public response: ClientResponse;

  public showResults: boolean = false;
  public rows: number;
  public warning: boolean = false;
  public warningMessage: string;

  constructor(private api: ApiService) {}

  ngOnInit(){
    this.model = new ClientRequest();
    this.model.amount = 0.00;
    this.api.getAllCountries().subscribe (
      (response) => {
        this.countries = response
      },(err: HttpErrorResponse) => {
        this.warningMessage = err.error.message;
        this.warning = true;
      }
    );
  }

  clearResults(){
    this.model = new ClientRequest();
    this.model.amount = 0.00;
    this.showResults = false;
    this.warning = false;
    this.warningMessage = "";
  }

  submitForm(){
    this.warning = false;
    this.warningMessage = "";
    this.api.sendRequest(this.model).subscribe(
      (response) => {
        this.showResults = true;
        this.response = response;
        this.rows = this.response.results.length-1;
      }
      ,(err: HttpErrorResponse) => {
        this.warningMessage = err.error.message;
        this.warning = true;
      }
    )
  }

  notLast(row:number):boolean{
    if (this.rows > row) return true;
    else return false;
  }

  checkLast(row: number): boolean{
    if (this.rows == row) return true;
    else return false;
  }

}
