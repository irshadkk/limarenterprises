import { Component, TemplateRef, OnInit } from '@angular/core';
import { DataService } from '../data.service';
import { BlockUI, NgBlockUI } from 'ng-block-ui';

@Component({
  templateUrl: 'register.component.html'
})
export class RegisterComponent implements OnInit {

  @BlockUI() blockUI: NgBlockUI;

  constructor(private dataService: DataService) { }

  ngOnInit() {

  }

}
