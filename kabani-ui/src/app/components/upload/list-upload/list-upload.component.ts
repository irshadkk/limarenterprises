import { Component, OnInit, ViewContainerRef, ViewChild } from '@angular/core';
import { HttpClient, HttpResponse, HttpEventType } from '@angular/common/http';
import { UploadFileService } from '../upload-file.service';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { DataService } from '../../../data.service';

// import { NotificationsService } from 'angular4-notify';


import { ToastsManager } from 'ng2-toastr/ng2-toastr';
import { ToastOptions } from 'ng2-toastr';

@Component({
  selector: 'list-upload',
  templateUrl: './list-upload.component.html',
  styleUrls: ['./list-upload.component.css']
})
export class ListUploadComponent implements OnInit {
  loading: boolean = false;
  @BlockUI() blockUI: NgBlockUI;
  @ViewChild('myInput') myInputVariable: any;
  selectedFiles: FileList
  currentFileUpload: File

  progressArr = [];

  constructor(private uploadService: UploadFileService, private dataService: DataService,  public toastr: ToastsManager, vcr: ViewContainerRef) {
    // constructor(private uploadService: UploadFileService, private dataService: DataService, protected notificationsService: NotificationsService, public toastr: ToastsManager, vcr: ViewContainerRef) {
    this.toastr.setRootViewContainerRef(vcr);
  }

  ngOnInit() {
    if (this.dataService.appDefined()) {

    }
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  }

  upload() {

    //let url='http://localhost:9000/demo/upload/'+this.dataService.getBranch();
    // this.notificationsService.notifications.closed;
    let fileNum = 0;

    let url = this.dataService.serviceurl + 'upload/branch1';
    fileNum = this.selectedFiles.length;
    if (this.selectedFiles.length > 0) {
      for (var i = 0; i < this.selectedFiles.length; i++) {
        this.blockUI.start("uploading the file.." + i);
        this.loading = true;
        let progress = { percentage: 0 }
        this.currentFileUpload = this.selectedFiles.item(i)

        this.uploadService.pushFileToStorage(this.currentFileUpload, url).subscribe(

          event => {
            if (event.type === HttpEventType.UploadProgress) {
              progress.percentage = Math.round(100 * event.loaded / event.total);
              this.progressArr.push(progress)

            } else if (event instanceof HttpResponse) {

              let res = event.body + '';
              this.blockUI.update(' file :-' + res + " is  completely uploaded!");
              this.toastr.success(' file :-' + res + " is  completely uploaded!", 'Success!', { duration: 1500, dismiss: 'auto' });
              setTimeout(() => {
                this.blockUI.stop();
                this.loading = false;
              }, 1500);

            }
          },
          error => {
            setTimeout(() => {
              this.blockUI.stop();
              this.loading = false;
            }, 1500);
            this.toastr.error('Error in  uploadeding files', 'Failed!', { showCloseButton: true, dismiss: 'click' });
          })
      }
    } else {
      setTimeout(() => {
        this.blockUI.stop();
        this.loading = false;
      }, 1500);
      this.toastr.error('No files selected,Please select some csv files to upload', 'Failed!', { showCloseButton: true, dismiss: 'click' });
    }




    this.myInputVariable.nativeElement.value = "";
  }
}
