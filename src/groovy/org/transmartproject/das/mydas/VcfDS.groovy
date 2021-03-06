package org.transmartproject.das.mydas

import transmart.mydas.VcfServiceAbstract
import uk.ac.ebi.mydas.configuration.DataSourceConfiguration
import uk.ac.ebi.mydas.configuration.PropertyType
import uk.ac.ebi.mydas.datasource.RangeHandlingAnnotationDataSource
import uk.ac.ebi.mydas.exceptions.BadReferenceObjectException
import uk.ac.ebi.mydas.exceptions.CoordinateErrorException
import uk.ac.ebi.mydas.exceptions.DataSourceException
import uk.ac.ebi.mydas.exceptions.UnimplementedFeatureException
import uk.ac.ebi.mydas.model.DasAnnotatedSegment
import uk.ac.ebi.mydas.model.DasEntryPoint
import uk.ac.ebi.mydas.model.DasType

import javax.servlet.ServletContext

/**
 * Created by j.hudecek on 5-3-14.
 */
class VcfDS implements RangeHandlingAnnotationDataSource {

    VcfServiceAbstract vcfService
    Long resultInstanceId
    String conceptKey

    List<DasEntryPoint> entryPoints

    @Override
    void init(ServletContext servletContext, Map<String, PropertyType> stringPropertyTypeMap, DataSourceConfiguration dataSourceConfiguration) throws DataSourceException {
        resultInstanceId = dataSourceConfiguration.getMatcherAgainstDsn().group(1).toLong()
        def ckEncoded = dataSourceConfiguration.getMatcherAgainstDsn().group(2)
        if (ckEncoded) {
            conceptKey = new String(ckEncoded.decodeBase64())
        }
    }

    @Override
    void destroy() {

    }

    @Override
    DasAnnotatedSegment getFeatures(String segmentId, Integer maxbins) throws BadReferenceObjectException, DataSourceException {
        vcfService.getFeatures(resultInstanceId, conceptKey, [segmentId], maxbins).first()
    }

    @Override
    DasAnnotatedSegment getFeatures(String segmentId, int start, int stop, Integer maxbins) throws BadReferenceObjectException, CoordinateErrorException, DataSourceException {
        vcfService.getFeatures(resultInstanceId, conceptKey, [segmentId], maxbins, new uk.ac.ebi.mydas.model.Range(start, stop)).first()
    }

    @Override
    DasAnnotatedSegment getFeatures(String segmentId, int start, int stop, Integer maxbins, uk.ac.ebi.mydas.model.Range range) throws BadReferenceObjectException, CoordinateErrorException, DataSourceException, UnimplementedFeatureException {
        vcfService.getFeatures(resultInstanceId, conceptKey, [segmentId], maxbins, range).first()
    }



    @Override
    DasAnnotatedSegment getFeatures(String segmentId, Integer maxbins, uk.ac.ebi.mydas.model.Range range) throws BadReferenceObjectException, DataSourceException, UnimplementedFeatureException {
        vcfService.getFeatures(resultInstanceId, conceptKey, [segmentId], maxbins, range).first()
    }

    @Override
    Collection<DasAnnotatedSegment> getFeatures(Collection<String> segmentIds, Integer maxbins, uk.ac.ebi.mydas.model.Range range) throws UnimplementedFeatureException, DataSourceException {
        vcfService.getFeatures(resultInstanceId, conceptKey, segmentIds, maxbins, range)
    }

    @Override
    Collection<DasAnnotatedSegment> getFeatures(Collection<String> segmentIds, Integer maxbins) throws UnimplementedFeatureException, DataSourceException {
        vcfService.getFeatures(resultInstanceId, conceptKey, segmentIds, maxbins)
    }

    @Override
    Collection<DasType> getTypes() throws DataSourceException {
        // TODO
        return null
    }

    @Override
    Integer getTotalCountForType(DasType dasType) throws DataSourceException {
        // TODO
        return null
    }

    @Override
    URL getLinkURL(String field, String id) throws UnimplementedFeatureException, DataSourceException {
        // TODO
        return null
    }

    @Override
    Collection<DasEntryPoint> getEntryPoints(Integer integer, Integer integer2) throws UnimplementedFeatureException, DataSourceException {
        // TODO
        return null
    }

    @Override
    String getEntryPointVersion() throws UnimplementedFeatureException, DataSourceException {
        // TODO
        return null
    }

    @Override
    int getTotalEntryPoints() throws UnimplementedFeatureException, DataSourceException {
        // TODO
        return 0
    }

}
