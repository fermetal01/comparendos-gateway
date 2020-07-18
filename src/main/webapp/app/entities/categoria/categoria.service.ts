import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ICategoria } from 'app/shared/model/categoria.model';

type EntityResponseType = HttpResponse<ICategoria>;
type EntityArrayResponseType = HttpResponse<ICategoria[]>;

@Injectable({ providedIn: 'root' })
export class CategoriaService {
  public resourceUrl = SERVER_API_URL + 'api/categorias';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/categorias';

  constructor(protected http: HttpClient) {}

  create(categoria: ICategoria): Observable<EntityResponseType> {
    return this.http.post<ICategoria>(this.resourceUrl, categoria, { observe: 'response' });
  }

  update(categoria: ICategoria): Observable<EntityResponseType> {
    return this.http.put<ICategoria>(this.resourceUrl, categoria, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ICategoria>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICategoria[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICategoria[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
