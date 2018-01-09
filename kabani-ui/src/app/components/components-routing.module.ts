import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
 
import { UploadCsvComponent } from './uploadcsv/uploadcsv.component';
import { DetailsUploadComponent } from './upload/details-upload/details-upload.component';
import { FormUploadComponent } from './upload/form-upload/form-upload.component';
import { ListUploadComponent } from './upload/list-upload/list-upload.component';

 
 
import { SwitchesComponent } from './switches.component';
import { ViewAttendanceComponent } from './viewattendance/viewattendance.component';
import { ViewSalaryComponent } from './viewsalary/viewsalary.component';

 

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
        path: 'details-upload',
        component: DetailsUploadComponent,
        data: {
          title: 'Details Upload Component '
        }
      },
      {
        path: 'form-upload',
        component: FormUploadComponent,
        data: {
          title: 'Form Upload Component '
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
        path: 'viewsalary',
        component: ViewSalaryComponent,
        data: {
          title: 'View Salary'
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
