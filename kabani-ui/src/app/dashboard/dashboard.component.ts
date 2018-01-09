import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DataService } from '../data.service';

@Component({
  templateUrl: 'dashboard.component.html'
})
export class DashboardComponent {
  public employeeAttendanceArr=[];
  public employeeAttendanceArrProcessed=[];

  constructor(private dataService:DataService) {
     
    this.loadAttendance();
  }
  loadAttendance() { 
    this.dataService.getPostData('http://kabanip-dev.us-east-1.elasticbeanstalk.com/getDistinctEmployeesO',null).subscribe(data => {
       
      this.employeeAttendanceArr=data;
      this.employeeAttendanceArr.forEach((item, index) => {
        let obj={name:""};
        obj.name=item[1];
        obj[item[2]]=item[0];

        let nameArr=this.employeeAttendanceArrProcessed.map(function(val){return val.name})
        index=nameArr.indexOf(item[1])
        console.log(index)
        if(index<0){
            this.employeeAttendanceArrProcessed.push(obj);
        }else{
            this.employeeAttendanceArrProcessed[index][item[2]]=item[0];

        }
        

      });

     console.log("----"+JSON.stringify(this.employeeAttendanceArrProcessed))

     });
  }
 
}
