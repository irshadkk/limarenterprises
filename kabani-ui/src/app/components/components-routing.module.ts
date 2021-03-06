import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
 
import { UploadCsvComponent } from './uploadcsv/uploadcsv.component'; 
import { ListUploadComponent } from './upload/list-upload/list-upload.component';
import { EmployeeUploadComponent } from './upload/employee-upload/employee-upload.component';

 
 
import { SwitchesComponent } from './switches.component';
import { ViewAttendanceComponent } from './viewattendance/viewattendance.component';
import { ViewSalaryComponent } from './viewsalary/viewsalary.component';
import { ViewEmployeesComponent } from './viewemployees/viewemployees.component';
import { ViewleavesummaryComponent } from './viewleavesummary/viewleavesummary.component'; 
import { AddholidaysComponent } from './addholidays/addholidays.component'; 
import { AddPresentComponent } from './addpresent/addpresent.component'; 



 

const routes: Routes = [
  {
    path: '',
    data: {
      title: 'Components'
    },
    children: [
      
      {
        path: 'uploadcsv',
        component: UploadCsvComponent,
        data: {
          title: 'Upload CSV '
        }
      },  
      {
        path: 'list-upload',
        component: ListUploadComponent,
        data: {
          title: 'List Upload Component '
        }
      }, 
      {
        path: 'employee-upload',
        component: EmployeeUploadComponent,
        data: {
          title: 'Employee Upload  '
        }
      }, 
      {
        path: 'switches',
        component: SwitchesComponent,
        data: {
          title: 'Switches'
        }
      },
      {
        path: 'viewattendance',
        component: ViewAttendanceComponent,
        data: {
          title: 'View Attendance'
        }
      },
      {
        path: 'viewemployees',
        component: ViewEmployeesComponent,
        data: {
          title: 'View Employees'
        }
      },
      {
        path: 'viewleavesummary',
        component: ViewleavesummaryComponent,
        data: {
          title: 'View Employees'
        }
      },
      {
        path: 'viewsalary',
        component: ViewSalaryComponent,
        data: {
          title: 'View Salary'
        }
      },
      {
        path: 'addholidays',
        component: AddholidaysComponent,
        data: {
          title: 'Add Holidays'
        }
      },
      {
        path: 'addpresent',
        component: AddPresentComponent,
        data: {
          title: 'Add Present'
        }
      }
       
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ComponentsRoutingModule {}
