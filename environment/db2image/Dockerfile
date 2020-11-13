FROM ibmcom/db2
# src debezium sample
RUN mkdir -p /asncdctools/src 
ENV PATH="/opt/ibm/db2/V11.5/bin:${PATH}"
# Necessary DB tuning fod CDC
ADD asncdc_UDF.sql /asncdctools/src
ADD asncdcaddremove.sql /asncdctools/src
ADD asncdctables.sql /asncdctools/src
ADD asncdc.c /asncdctools/src
ADD dbsetup.sh /asncdctools/src

ADD startup-agent.sql /asncdctools/src
ADD build-tables.sql /asncdctools/src
ADD startup-cdc.sql /asncdctools/src
RUN chmod -R  777  /asncdctools

RUN mkdir /var/custom
ADD cdcsetup.sh /var/custom
RUN chmod -R  777 /var/custom

# ADD custom-init /var/custom-init
ADD openshift_entrypoint.sh /var/db2_setup/lib/
