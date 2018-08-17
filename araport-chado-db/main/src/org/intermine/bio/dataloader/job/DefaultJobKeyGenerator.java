package org.intermine.bio.dataloader.job;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of the {@link JobKeyGenerator} interface.
 * This implementation provides a single hash value based on the JobParameters
 * passed in.  Only identifying parameters (per {@link JobParameter#isIdentifying()})
 * are used in the calculation of the key.
 *
 */
public class DefaultJobKeyGenerator implements JobKeyGenerator<JobParameters>
{

    /**
     * Generates the job key to be used based on the {@link JobParameters} instance
     * provided.
     */
    @Override
    public String generateKey(JobParameters source) {

        Map<String, JobParameter> props = source.getParameters();
        StringBuffer stringBuffer = new StringBuffer();
        List<String> keys = new ArrayList<String>(props.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            JobParameter jobParameter = props.get(key);
            if (jobParameter.isIdentifying()) {
                String value = jobParameter.getValue() == null ? "" : jobParameter.toString();
                stringBuffer.append(key + "=" + value + ";");
            }
        }

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "MD5 algorithm not available.  Fatal (should be in the JDK).");
        }

        try {
            byte[] bytes = digest.digest(stringBuffer.toString().getBytes(
                    "UTF-8"));
            return String.format("%032x", new BigInteger(1, bytes));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(
                    "UTF-8 encoding not available.  Fatal (should be in the JDK).");
        }
    }
}
