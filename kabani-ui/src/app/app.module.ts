import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Http, Response,HttpModule } from '@angular/http';  
import {HttpClientModule} from '@angular/common/http'; 


import { AppComponent } from './app.component';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { NAV_DROPDOWN_DIRECTIVES } from './shared/nav-dropdown.directive';

import { ChartsModule } from 'ng2-charts/ng2-charts';
import { SIDEBAR_TOGGLE_DIRECTIVES } from './shared/sidebar.directive';
import { AsideToggleDirective } from './shared/aside.directive';
import { BreadcrumbsComponent } from './shared/breadcrumb.component';

// Routing Module
import { AppRoutingModule } from './app.routing';

// Services
import { DataService } from './data.service'; 

import {UploadFileService} from './components/upload/upload-file.service';
// Layouts
import { FullLayoutComponent } from './layouts/full-layout.component';
import { SimpleLayoutComponent } from './layouts/simple-layout.component';
import { LoginComponent } from './pages/login.component';
import { BlockUIModule } from 'ng-block-ui'; 
import {NotificationsModule, NotificationsService} from 'angular4-notify';

import {ToastModule} from 'ng2-toastr/ng2-toastr';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

@NgModule({
  imports: [
    BrowserModule,FormsModule, BrowserAnimationsModule, ToastModule.forRoot(),
    AppRoutingModule,
    BsDropdownModule.forRoot(),
    TabsModule.forRoot(), 
    ChartsModule, HttpModule,HttpClientModule,BlockUIModule,NotificationsModule
  ],
  declarations: [
  LoginComponent,
    AppComponent,
    FullLayoutComponent,
    SimpleLayoutComponent,
    NAV_DROPDOWN_DIRECTIVES,
    BreadcrumbsComponent,
    SIDEBAR_TOGGLE_DIRECTIVES,
    AsideToggleDirective,
  ],
  providers: [{
    provide: LocationStrategy,
    useClass: HashLocationStrategy
  },DataService,UploadFileService,NotificationsService],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
