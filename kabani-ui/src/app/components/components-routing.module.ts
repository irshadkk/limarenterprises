import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { UploadCsvComponent } from './uploadcsv/uploadcsv.component';
import { ListUploadComponent } from './upload/list-upload/list-upload.component';
import { EmployeeUploadComponent } from './upload/employee-upload/employee-upload.component';
import { IncomeTaxUploadComponent } from './upload/incometax-upload/incometax-upload.component';



import { SwitchesComponent } from './switches.component';
import { ViewAttendanceComponent } from './viewattendance/viewattendance.component';
import { ViewSalaryComponent } from './viewsalary/viewsalary.component';
import { HalfMonthComponent } from './viewsalary/half-month/half-month.component';

import { ViewEmployeesComponent } from './viewemployees/viewemployees.component';
import { ViewleavesummaryComponent } from './viewleavesummary/viewleavesummary.component';
import { AddholidaysComponent } from './addholidays/addholidays.component';
import { AddPresentComponent } from './addpresent/addpresent.component';

import { ManageLoanComponent } from './manage-loan/manage-loan.component';
import { AddIncomeTaxComponent } from './addincometax/addincometax.component';
import { ManageAdvanceComponent } from './manage-advance/manage-advance.component';
import { IncentiveComponent } from './incentive/incentive.component';


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
        path: 'incometax-upload',
        component: IncomeTaxUploadComponent,
        data: {
          title: 'Income Tax Upload  '
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
      }, {
        path: 'viewHalfMonthSalary',
        component: HalfMonthComponent,
        data: {
          title: 'View Mid Month Salary'
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
      },
      {
        path: 'manageLoan',
        component: ManageLoanComponent,
        data: {
          title: 'Manage Loan'
        }
      },
      {
        path: 'manageAdvance',
        component: ManageAdvanceComponent,
        data: {
          title: 'Manage Salary Advances'
        }
      },
      {
        path: 'manageIncentives',
        component: IncentiveComponent,
        data: {
          title: 'Manage Salary Advances'
        }
      },
      {
        path: 'manageincomeTax',
        component: AddIncomeTaxComponent,
        data: {
          title: 'Manage Income Tax'
        }
      }

    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ComponentsRoutingModule { }
