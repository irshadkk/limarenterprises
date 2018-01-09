import { Component } from '@angular/core';
import { DataService } from '../data.service';
import { ActivatedRoute, Router} from '@angular/router';


@Component({
  templateUrl: 'login.component.html'
})
export class LoginComponent {

  constructor(private dataService:DataService,private router: Router) { 
    this.name="limar";
    this.password="limar";
  }

  loadUser() {
    let link = ['/dashboard'];
    this.dataService.getUser(this.name,this.password).subscribe(data => { 
     if(data){
     this.dataService.setBranch(this.name);
       this.router.navigate(link);

     }else{
        alert("the user name or password is incorrect")
     }

     

     });
  }



  public name:any;
  public password:any;

}
