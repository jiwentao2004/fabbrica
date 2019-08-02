package in.co.futech.fabbricaserver.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {
    public static Query buildQuery( String filters, Pageable pageable ) {
        Query dynamicQuery = new Query();
        if(!filters.equals("")) {
            for( String filterString: filters.split(";")) {
                String[] filterStringArray = filterString.split(",");
                switch (filterStringArray[1]) {
                    case "=": dynamicQuery.addCriteria(Criteria.where(filterStringArray[0]).is(filterStringArray[2]));
                        break;
                    case "!=": dynamicQuery.addCriteria(Criteria.where(filterStringArray[0]).ne(filterStringArray[2]));
                        break;
                    case "<": dynamicQuery.addCriteria(Criteria.where(filterStringArray[0]).lt(filterStringArray[2]));
                        break;
                    case "<=": dynamicQuery.addCriteria(Criteria.where(filterStringArray[0]).lte(filterStringArray[2]));
                        break;
                    case ">": dynamicQuery.addCriteria(Criteria.where(filterStringArray[0]).gt(filterStringArray[2]));
                        break;
                    case ">=": dynamicQuery.addCriteria(Criteria.where(filterStringArray[0]).gte(filterStringArray[2]));
                        break;
                    case "like": dynamicQuery.addCriteria(Criteria.where(filterStringArray[0]).regex(filterStringArray[2]));
                        break;
                    case "not like": dynamicQuery.addCriteria(Criteria.where(filterStringArray[0]).regex(filterStringArray[2]));
                        break;
                    /** case "in": dynamicQuery.addCriteria(Criteria.where(filterStringArray[0]).in(filterString.substring(filterString.indexOf(filterStringArray[2])).split(",")));
                     break;
                     case "not in": dynamicQuery.addCriteria(Criteria.where(filterStringArray[0]).nin(filterString.substring(filterString.indexOf(filterStringArray[2])).split(",")));
                     break; **/
                }
            }
        }
        dynamicQuery.with(pageable);
        return dynamicQuery;
    }

    public static Pageable buildPageable(int page, int size, String sort) {
        Pageable pageable = null;
        if(!sort.equals("")) {
            List<Sort.Order> sortOrders = new ArrayList<>();
            for (String sortString : sort.split(",")) {
                switch (sortString.charAt(0)) {
                    case '-':
                        sortOrders.add(Sort.Order.desc(sortString.substring(1)));
                        break;
                    default:
                        sortOrders.add(Sort.Order.asc(sortString.substring(1)));
                        break;
                }
            }
            pageable = PageRequest.of(page, size, Sort.by(sortOrders));
        }
        else {
            pageable = PageRequest.of(page, size);
        }
        return pageable;
    }
}
