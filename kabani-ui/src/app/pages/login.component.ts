import { Component } from '@angular/core';
import { DataService } from '../data.service';
import { ActivatedRoute, Router } from '@angular/router';
import { BlockUI, NgBlockUI } from 'ng-block-ui';

@Component({
  templateUrl: 'login.component.html'
})
export class LoginComponent {
  @BlockUI() blockUI: NgBlockUI;
  constructor(private dataService: DataService, private router: Router) {
    this.name = "limar 1";
    this.password = "limar 1";
  }

  loadUser() {
    this.blockUI.start("Validating...");
    let link = ['/dashboard'];
    this.dataService.getUser(this.name, this.password).subscribe(data => {
      if (data) {
        setTimeout(() => {
          this.blockUI.stop();
          this.dataService.setBranch(this.name);
          this.router.navigate(link);
        }, 1000);


      } else {
        this.blockUI.update("The user name or password is incorrect.")
        setTimeout(() => {

          this.blockUI.stop();
        }, 2500);
      }



    });
  }



  public name: any;
  public password: any;

}
