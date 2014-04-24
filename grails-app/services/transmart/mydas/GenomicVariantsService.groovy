package transmart.mydas

import org.transmartproject.core.dataquery.highdim.chromoregion.RegionRow
import org.transmartproject.core.dataquery.highdim.vcf.VcfValues
import uk.ac.ebi.mydas.model.DasFeature
import uk.ac.ebi.mydas.model.DasType
import uk.ac.ebi.mydas.model.DasFeatureOrientation
import uk.ac.ebi.mydas.model.DasPhase

import javax.annotation.PostConstruct

/**
 * Created by j.hudecek on 6-3-14.
 */
class GenomicVariantsService  extends  VcfServiceAbstract {

    @PostConstruct
    void init() {
        super.init()
        dasTypes = [projectionName];
    }

    @Override
    protected void getSpecificFeatures(RegionRow region, Object assays,  Map<String, String> params, Collection<DasType> dasTypes, Map<String, List<DasFeature>> featuresPerSegment) {
        constructSegmentFeaturesMap([region], getGenomicTypeFeature, featuresPerSegment)
    }

    private def getGenomicTypeFeature = { VcfValues val ->

        def linkMap = val.rsId == '.' ? [:]
                : [(new URL("http://www.ncbi.nlm.nih.gov/projects/SNP/snp_ref.cgi?rs=${val.rsId}")): 'NCBI SNP Ref']

        def results = []
        val.genomicVariantTypes.eachWithIndex { genomicVariantType, indx ->
            results << new DasFeature(
                    // feature id - any unique id that represent this feature
                    "gv-${val.rsId}-$genomicVariantType",
                    // feature label
                    'Genomic Variant Type',
                    // das type
                    new DasType(genomicVariantType.toString(), "", "", ""),
                    // das method TODO: pls find out what is actually means
                    dasMethod,
                    // start pos
                    val.position.toInteger(),
                    // end pos
                    val.position.toInteger(),
                    // value - this is where Minor Allele Freq (MAF) value is placed
                    null,
                    DasFeatureOrientation.ORIENTATION_NOT_APPLICABLE,
                    DasPhase.PHASE_NOT_APPLICABLE,
                    //notes
                    ["RefSNP=${val.rsId}",
                            "REF=${val.referenceAllele}",
                            "ALT=${val.alternativeAlleles.join(',')}",
                            "CurrentALT=${val.alternativeAlleles[indx]}",
                            "AlleleCount=${val.additionalInfo['AC'] ?: NA}",
                            "AlleleFrequency=${val.additionalInfo['AF'] ?: NA}",
                            "TotalAllele=${val.additionalInfo['AN'] ?: NA}",
                            "VariantClassification=${val.additionalInfo['VC'] ?: NA}",
                            "MAF=${val.maf ? String.format('%.2f', val.maf) : NA}",
                            "QualityOfDepth=${val.qualityOfDepth ?: NA}"]*.toString(),
                    //links
                    linkMap,
                    //targets
                    [],
                    //parents
                    [],
                    //parts
                    []
            )
        }

        results
    }
}
