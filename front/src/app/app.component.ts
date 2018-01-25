import { Component, OnInit } from '@angular/core';

import {ApiService} from './TBA-service/api.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  public title: String = '';

  constructor(private api: ApiService) {}

  public ngOnInit(){
    this.api.getMessage(1).subscribe(
      (response) => {
        this.title = response.content;
      }
    );
  }
}
