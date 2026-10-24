import { Component, input } from '@angular/core';
@Component({
  selector: 'app-loading-skeleton',
  template: `<div class="loading-state" role="status"><p><i class="pi pi-spin pi-spinner" aria-hidden="true"></i> {{ label() }}</p><div aria-hidden="true" class="skeleton-lines"><span></span><span></span><span></span></div></div>`,
})
export class LoadingSkeletonComponent { readonly label = input('Cargando datos…'); }
