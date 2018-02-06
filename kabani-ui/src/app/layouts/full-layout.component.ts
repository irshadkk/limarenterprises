import { Component, TemplateRef, OnInit } from '@angular/core';
import { DataService } from '../data.service';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './full-layout.component.html'
})
export class FullLayoutComponent implements OnInit {
  @BlockUI() blockUI: NgBlockUI;

  constructor(private router: Router,private dataService: DataService) {
  }

  ngOnInit() {
    if(this.dataService.appDefined()){
      
    }
  }
  public disabled = false;
  public status: {isopen: boolean} = {isopen: false};

  public toggled(open: boolean): void {
    console.log('Dropdown is now: ', open);
  }

  public toggleDropdown($event: MouseEvent): void {
    $event.preventDefault();
    $event.stopPropagation();
    this.status.isopen = !this.status.isopen;
  }
  logout(){
    localStorage.clear();
    this.router.navigate(['/login']);
   }

}
