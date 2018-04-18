import { Component,ViewContainerRef  } from '@angular/core';
import { DataService } from '../data.service'; 
import { ActivatedRoute, Router } from '@angular/router';
import { BlockUI, NgBlockUI } from 'ng-block-ui'; 
import { ToastsManager } from 'ng2-toastr/ng2-toastr';
import {ToastOptions} from 'ng2-toastr';

@Component({
  templateUrl: 'login.component.html'
})
export class LoginComponent {
  
  @BlockUI() blockUI: NgBlockUI;
  constructor(private dataService: DataService, private router: Router ,public toastr: ToastsManager, vcr: ViewContainerRef) {
    this.name = "limar 1";
    this.password = "limar 1";
    this.toastr.setRootViewContainerRef(vcr);
  }

  loadUser() {
    // this.notificationsService.addInfo('Information message');
    this.blockUI.start("Authenticating...");
    let link = ['components/viewattendance'];
    this.dataService.getUser(this.name, this.password).subscribe(data => {
      if (data) {
          this.toastr.success('You are successfully logged in !', 'Success!');
        setTimeout(() => {
          this.blockUI.stop();
          this.dataService.setBranch(this.name);
          this.router.navigate(link);
        }, 1000);


      } else {
        //  this.notificationsService.addError('The user name or password is incorrect.');
        //  this.notificationsService.addWarning('Some warning message');
        this.blockUI.update("The user name or password is incorrect.")
        this.toastr.error('The user name or password is incorrect.!', 'Failed!', { showCloseButton: true,dismiss: 'click'});
        setTimeout(() => {

          this.blockUI.stop();
        }, 2500);
      }



    },
  error  => {
   // this.notificationsService.addError("Error Contacting server"+error);
  //  this.notificationsService.addWarning('Some warning message');
    this.blockUI.update("Error Contacting server"+error) 
    this.toastr.error("Error Contacting server"+error, 'Failed!', { showCloseButton: true,dismiss: 'click'});
        setTimeout(() => {

          this.blockUI.stop();
        }, 2500);
  });
  }



  public name: any;
  public password: any;

}
